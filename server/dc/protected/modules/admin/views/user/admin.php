<?php
/* @var $this UserController */
/* @var $model User */

$this->breadcrumbs=array(
	'Users'=>array('index'),
	'Manage',
);

$this->menu=array(
	array('label'=>'List User', 'url'=>array('index')),
	array('label'=>'Create User', 'url'=>array('create')),
);

Yii::app()->clientScript->registerScript('search', "
$('.search-button').click(function(){
	$('.search-form').toggle();
	return false;
});
$('.search-form form').submit(function(){
	$('#user-grid').yiiGridView('update', {
		data: $(this).serialize()
	});
	return false;
});
");
?>

<h1>Manage Users</h1>

<p>
You may optionally enter a comparison operator (<b>&lt;</b>, <b>&lt;=</b>, <b>&gt;</b>, <b>&gt;=</b>, <b>&lt;&gt;</b>
or <b>=</b>) at the beginning of each of your search values to specify how the comparison should be done.
</p>

<?php echo CHtml::link('Advanced Search','#',array('class'=>'search-button')); ?>
<div class="search-form" style="display:none">
<?php $this->renderPartial('_search',array(
	'model'=>$model,
)); ?>
</div><!-- search-form -->

<?php $this->widget('zii.widgets.grid.CGridView', array(
	'id'=>'user-grid',
	'dataProvider'=>$model->search(),
	'filter'=>$model,
	'columns'=>array(
		#'usr_id',
		#'wechat',
		'name',
	    array('name'=>'gender','value'=>'$data->gender==1?"公":"母"', 'filter'=>array(1=>"公",2=>"母")),
		#'tx',
		'age',
		array('name'=>'type', 'value'=>'User::model()->getTypeName($data->type)', 'filter'=>Util::loadConfig('pet_type')),
		'code',
		'weibo',
		#'inviter',
        array('name'=>'create_time','value'=>'date("Y-m-d H:i:s",$data->create_time)'),
		#'update_time',
		array(
			'class'=>'CButtonColumn',
		),
	),
)); ?>

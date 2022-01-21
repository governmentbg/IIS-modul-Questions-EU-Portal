<?php

namespace App\Models;

use App\Models\MNewsImg;
use App\Models\MNewsLng;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Spatie\EloquentSortable\Sortable;
use Spatie\EloquentSortable\SortableTrait;


/**
 * @property int        $M_News_id
 * @property Date       $M_News_date
 * @property string     $M_News_name
 * @property int        $M_News_order
 * @property string     $M_News_video
 * @property int        $A_ns_C_id
 * @property int        $MT_id
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MNews extends Model
// class MNews extends Model  implements Sortable
{
    use SoftDeletes;

    use SortableTrait;

    public $sortable = [
        'order_column_name' => 'M_News_order',
        'sort_when_creating' => true,
    ];

    // public function buildSortQuery()
    // {
    //     return static::query()->where('M_News_date', 'M_News_date');
    // }

    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'M_News';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'M_News_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'M_News_date', 'M_News_name', 'M_News_order', 'M_News_video', 'M_News_rs_pos', 'A_ns_C_id', 'MT_id', 'M_News_gal', 'M_News_lead', 'C_St_id'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'M_News_id' => 'int', 'M_News_date' => 'date', 'M_News_name' => 'string', 'M_News_order' => 'int', 'M_News_video' => 'string', 'A_ns_C_id' => 'int', 'MT_id' => 'int', 'M_News_gal' => 'int', 'M_News_lead' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'M_News_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_news_lang()
    {
        return $this->hasMany(MNewsLng::class, 'M_News_id');
    }
    public function eq_gallery()
    {
        return $this->hasMany(MNewsImg::class, 'M_News_id');
    }

    public function eq_event()
    {
        return $this->hasMany(GGalleryEvents::class, 'M_News_id');
    }
}
